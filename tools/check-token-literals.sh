#!/usr/bin/env bash
# G2 · 令牌字面量扫描（wisdomCheckTokenLiterals）
# 扫描手写 Kotlin 源，禁止 Color(0x...)/#RRGGBB/裸 dp|sp|em。
# generated/** 为生成物，排除（LR-13）。只读检查。
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SRC="$ROOT/wisdom-ui/src"
FAIL=0

while IFS= read -r -d '' f; do
  rel="${f#"$ROOT/"}"
  case "$rel" in
    wisdom-ui/src/main/kotlin/io/github/wlunc/wisdom/foundation/generated/*) continue ;;
    wisdom-ui/src/test/kotlin/io/github/wlunc/wisdom/internal/*) continue ;;
  esac
  while IFS=: read -r line_no line; do
    line="${line%%//*}"
    if printf '%s' "$line" | grep -Eq 'Color\(0x[0-9A-Fa-f]+\)'; then
      echo "[G2][FAIL] $rel:$line_no  Color(0x...) 裸色值" >&2; FAIL=1
    fi
    if printf '%s' "$line" | grep -Eq '#[0-9A-Fa-f]{6}([^0-9A-Fa-f]|$)'; then
      echo "[G2][FAIL] $rel:$line_no  #RRGGBB 裸色值" >&2; FAIL=1
    fi
    if printf '%s' "$line" | grep -Eq '(^|[^A-Za-z0-9_.])[0-9]+(\.[0-9]+)?\.(dp|sp|em)([^A-Za-z0-9_]|$)'; then
      echo "[G2][FAIL] $rel:$line_no  裸 dp/sp/em 字面量" >&2; FAIL=1
    fi
  done < <(grep -n '' "$f" || true)
done < <(find "$SRC" -type f -name '*.kt' -print0)

if [[ "$FAIL" -ne 0 ]]; then
  echo "[G2] 令牌字面量检查失败；值必须走 WDSize/WDType/WDSpacing/WDComponent/WDMotion/WDElevation。" >&2
  exit 1
fi
echo "[G2] PASS：手写源无 Color(0x)/#RRGGBB/裸 dp|sp|em（generated/** 已排除）"
