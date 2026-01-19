#!/usr/bin/env bash
# verify_endpoints.sh
# Small bash script using curl to quickly verify endpoints. Usage: ./scripts/verify_endpoints.sh [baseUrl]

BASE_URL=${1:-http://localhost:8080}

set -euo pipefail

function run_check() {
  desc="$1"
  method="$2"
  path="$3"
  data="$4"
  expected="$5"

  url="$BASE_URL$path"
  if [ -n "$data" ]; then
    out=$(curl -s -w "\n%{http_code}" -X "$method" -H "Content-Type: application/json" -d "$data" "$url")
  else
    out=$(curl -s -w "\n%{http_code}" -X "$method" "$url")
  fi

  code=$(echo "$out" | tail -n1)
  body=$(echo "$out" | sed '$d')

  if [ "$code" -eq "$expected" ]; then
    echo "$desc -> $code OK"
    return 0
  else
    echo "$desc -> $code FAIL"
    echo "URL: $url"
    echo "Body:" >&2
    echo "$body" >&2
    return 1
  fi
}

echo "Running quick endpoint verification against $BASE_URL"

# GET /ninjas
run_check "GET /ninjas" GET "/ninjas" "" 200 || true

# GET /ninjas/1
run_check "GET /ninjas/1" GET "/ninjas/1" "" 200 || true

# POST /ninjas
uniq=$(head /dev/urandom | tr -dc a-z0-9 | head -c8)
post_data="{\"nome\":\"sh-$uniq\",\"email\":\"sh-$uniq@example.test\",\"idade\":25}"
post_out=$(curl -s -w "\n%{http_code}" -X POST -H "Content-Type: application/json" -d "$post_data" "$BASE_URL/ninjas")
post_code=$(echo "$post_out" | tail -n1)
post_body=$(echo "$post_out" | sed '$d')

if [ "$post_code" -ne 201 ]; then
  echo "POST /ninjas failed with $post_code" >&2
  echo "$post_body" >&2
  exit 1
fi

# Extract id via regex (e.g., "id":123)
new_id=$(echo "$post_body" | tr -d '\n' | sed -n 's/.*"id"\s*:\s*\([0-9][0-9]*\).*/\1/p')
if [ -z "$new_id" ]; then
  echo "Could not determine created id from POST response" >&2
  exit 1
fi

# PATCH new id
patch_data="{\"nome\":\"sh-patched-$uniq\"}"
run_check "PATCH /ninjas/$new_id" PATCH "/ninjas/$new_id" "$patch_data" 200

# GET verify
run_check "GET /ninjas/$new_id" GET "/ninjas/$new_id" "" 200

# DELETE
run_check "DELETE /ninjas/$new_id" DELETE "/ninjas/$new_id" "" 204

# GET after delete
run_check "GET /ninjas/$new_id (after delete)" GET "/ninjas/$new_id" "" 404 || true

echo "All checks passed"
