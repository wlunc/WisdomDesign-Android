#!/usr/bin/env bash
# G13 · L-B 标点/词序字面量扫描（wisdomCheckLiteralLanguage）
# 手写 src/main（排除 generated/**）不得含文档级标点或词序模板；M0–M2 warning。
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SRC="$ROOT/wisdom-ui/src/main/kotlin"
if [[ ! -d "$SRC" ]]; then echo "[G13][FAIL] 缺源目录: $SRC" >&2; exit 1; fi

PAT='，。、；：！？|第|共|关闭| of '
HITS=0
while IFS= read -r -d '' f; do
  rel="${f#"$ROOT/"}"
  case "$rel" in
    */foundation/generated/*) continue ;;
    */.agent-teams/*) continue ;;
  esac
  while IFS=: read -r line_no line; do
    [[ "$line" == *"wd-literal-check:disable"* ]] && continue
    if printf '%s' "$line" | grep -Eq "$PAT"; then
      echo "[G13][WARN] $rel:$line_no 命中文档级标点/词序模板" >&2; HITS=$((HITS+1))
    fi
  done < <(grep -n '' "$f" || true)
done < <(find "$SRC" -type f -name '*.kt' -print0)

echo "[G13] M0–M2 warning：命中 $HITS 处；M3 起转 error（当前 exit 0）"
