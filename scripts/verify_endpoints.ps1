# verify_endpoints.ps1
# Script PowerShell que usa curl.exe para testar endpoints da API localmente.
# Uso: .\scripts\verify_endpoints.ps1 [baseUrl]

param(
    [string]$BaseUrl = "http://localhost:8080"
)

$results = @()

function Run-CurlCheck {
    param(
        [string]$Desc,
        [string]$Method,
        [string]$Path,
        [string]$Data,
        [int]$ExpectedCode
    )

    $url = "$BaseUrl$Path"
    $tmp = [System.IO.Path]::GetTempFileName()

    $args = @('-s','-w',"`n%{http_code}")
    if ($Method) { $args += '-X'; $args += $Method }
    if ($Data) { $args += '-H'; $args += 'Content-Type: application/json'; $args += '-d'; $args += $Data }
    $args += $url

    try {
        & curl.exe @args > $tmp
    } catch {
        Write-Host "ERROR running curl: $_" -ForegroundColor Red
        return @{desc=$Desc; code=0; ok=$false; body="curl error: $_"}
    }

    $content = Get-Content -Raw -Path $tmp
    Remove-Item $tmp -ErrorAction SilentlyContinue

    # split off the final status code on the last line
    $lines = $content -split "`n"
    $code = 0
    if ($lines.Length -ge 1) {
        $last = $lines[-1]
        if (-not [int]::TryParse($last,[ref]$code)) { $code = 0 }
        if ($lines.Length -gt 1) { $body = ($lines[0..($lines.Length-2)] -join "`n").Trim() } else { $body = '' }
    } else {
        $body = ''
    }

    $ok = ($code -eq $ExpectedCode)
    if ($ok) { $status = 'OK' } else { $status = 'FAIL' }
    Write-Host "$Desc -> $code ($status)"
    if (-not $ok) {
        Write-Host "URL: $url"
        Write-Host "Response body:" -ForegroundColor Yellow
        Write-Host $body
    }
    return @{desc=$Desc; code=$code; ok=$ok; body=$body}
}

Write-Host "Running quick endpoint verification against $BaseUrl" -ForegroundColor Cyan

# 1 - GET /ninjas
$results += Run-CurlCheck -Desc 'GET /ninjas' -Method 'GET' -Path '/ninjas' -ExpectedCode 200

# 2 - GET /ninjas/1 (seeded data.sql should have id=1)
$results += Run-CurlCheck -Desc 'GET /ninjas/1' -Method 'GET' -Path '/ninjas/1' -ExpectedCode 200

# 3 - POST /ninjas (create new)
$uniq = [guid]::NewGuid().ToString().Substring(0,8)
$postObj = @{ nome = "ps-test-$uniq"; email = "ps-$uniq@example.test"; idade = 25 }
$postData = $postObj | ConvertTo-Json -Compress
$post = Run-CurlCheck -Desc 'POST /ninjas' -Method 'POST' -Path '/ninjas' -Data $postData -ExpectedCode 201
$results += $post

# try to parse id from body if created
$newId = $null
if ($post.code -eq 201 -and $post.body) {
    try {
        $obj = $post.body | ConvertFrom-Json -ErrorAction Stop
        if ($obj -and $obj.id) { $newId = $obj.id }
    } catch {
        Write-Host "Warning: não foi possível parsear JSON do POST response: $_" -ForegroundColor Yellow
    }
}

if (-not $newId) {
    Write-Host "Cannot continue PATCH/DELETE checks because created id is unknown." -ForegroundColor Yellow
} else {
    # 4 - PATCH update name
    $patchObj = @{ nome = "ps-patched-$uniq" }
    $patchData = $patchObj | ConvertTo-Json -Compress
    $results += Run-CurlCheck -Desc "PATCH /ninjas/$newId" -Method 'PATCH' -Path "/ninjas/$newId" -Data $patchData -ExpectedCode 200

    # 5 - GET to verify patched
    $getAfter = Run-CurlCheck -Desc "GET /ninjas/$newId" -Method 'GET' -Path "/ninjas/$newId" -ExpectedCode 200
    $results += $getAfter
    if ($getAfter.body -and $getAfter.body -like "*ps-patched-$uniq*") {
        Write-Host "Patch verification: name updated OK"
    } else {
        Write-Host "Patch verification: name not found in response" -ForegroundColor Yellow
    }

    # 6 - DELETE the created record
    $results += Run-CurlCheck -Desc "DELETE /ninjas/$newId" -Method 'DELETE' -Path "/ninjas/$newId" -ExpectedCode 204

    # 7 - GET again expecting 404
    $results += Run-CurlCheck -Desc "GET /ninjas/$newId (after delete)" -Method 'GET' -Path "/ninjas/$newId" -ExpectedCode 404
}

# summary
$failed = $results | Where-Object { -not $_.ok }
Write-Host "------------------------------"
if ($failed.Count -eq 0) {
    Write-Host "All checks passed." -ForegroundColor Green
    exit 0
} else {
    Write-Host "$($failed.Count) checks failed:" -ForegroundColor Red
    $failed | ForEach-Object { Write-Host " - $($_.desc) -> $($_.code)" }
    exit 1
}
