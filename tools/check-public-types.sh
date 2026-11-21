#!/usr/bin/env bash
# G9 · 公开类型正向白名单（wisdomCheckPublicTypes）
# 解析 api/wisdom-ui.api 全部 L...; 类型引用，只允许指定前缀；/internal/ 出现即 fail。
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
API="$ROOT/wisdom-ui/api/wisdom-ui.api"
if [[ ! -f "$API" ]]; then echo "[G9][FAIL] 缺 API 基线: $API" >&2; exit 1; fi

ALLOW_RE='^L(io/github/wlunc/wisdom/|androidx/compose/(ui|foundation|runtime|animation)/|kotlin/|java/)'
FAIL=0
while IFS= read -r line; do
  # 抽取每行中的 L...; 描述符
  while IFS= read -r desc; do
    [[ -z "$desc" ]] && continue
    if [[ "$desc" =~ ^Lio/github/wlunc/wisdom/internal/ ]]; then
      echo "[G9][FAIL] internal 类型出现在 ABI: $desc" >&2; FAIL=1
      continue
    fi
    if ! printf '%s' "$desc" | grep -Eq "$ALLOW_RE"; then
      echo "[G9][FAIL] 非白名单类型: $desc" >&2; FAIL=1
    fi
  done < <(printf '%s\n' "$line" | grep -oE 'L[A-Za-z0-9_/$]+;' || true)
done < "$API"

if [[ "$FAIL" -ne 0 ]]; then exit 1; fi
echo "[G9] PASS：API 基线类型引用全部落在白名单前缀，无 material3/internal/activity/window"
