#!/usr/bin/env bash
# G7 · 生成物 banner hash 自证（wisdomCheckGeneratedSelfProof）
# 断言生成物 banner 与 WDTokensVersion.sha256 一致；解析失败 fail。
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
GEN="$ROOT/wisdom-ui/src/main/kotlin/io/github/wlunc/wisdom/foundation/generated"
TOKENS="$GEN/WDTokens.kt"
VER="$GEN/WDTokensVersion.kt"
for f in "$TOKENS" "$VER"; do
  if [[ ! -f "$f" ]]; then echo "[G7][FAIL] 缺生成物: $f" >&2; exit 1; fi
done

BANNER="$(head -8 "$TOKENS" | tr -d '\r' | sed 's|^//[[:space:]]*||' | grep -m1 -E '^tokens v[0-9.]+ · sha256:[0-9a-f]{12}$' || true)"
if [[ -z "$BANNER" ]]; then
  echo "[G7][FAIL] banner 解析失败（前 8 行未找到 tokens v... · sha256:...）" >&2; exit 1
fi
BANNER_HASH="${BANNER##*sha256:}"
BANNER_VER="${BANNER#tokens v}"; BANNER_VER="${BANNER_VER%% *}"
VER_HASH="$(grep -m1 -E 'sha256: String = "[0-9a-f]+"' "$VER" | sed 's/.*"\([0-9a-f]*\)".*/\1/')"
VER_VERSION="$(sed -n 's/.*version: String = "\([^"]*\)".*/\1/p' "$VER" | head -1)"
if [[ "$VER_HASH" != "$BANNER_HASH" ]]; then
  echo "[G7][FAIL] banner hash ($BANNER_HASH) != WDTokensVersion.sha256 ($VER_HASH)" >&2; exit 1
fi
if [[ -n "$VER_VERSION" && "$VER_VERSION" != "$BANNER_VER" ]]; then
  echo "[G7][FAIL] banner version ($BANNER_VER) != WDTokensVersion.version ($VER_VERSION)" >&2; exit 1
fi
echo "[G7] PASS：banner tokens v$BANNER_VER · sha256:$BANNER_HASH 与 WDTokensVersion 一致"
