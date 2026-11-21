#!/usr/bin/env bash
# G8 · release AAR 预览符号检查（wisdomCheckReleaseSymbols）
# release classes.jar 中 Preview 计数 == 0；debug classes.jar 中 > 0（正例）。
# 接受 AAR 路径或目录作为位置参数；Gradle 任务由 wisdom-ui/build.gradle.kts 传入。
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BUILD="$ROOT/wisdom-ui/build/outputs/aar"
RELEASE="${1:-$BUILD/wisdom-ui-release.aar}"
DEBUG="${2:-$BUILD/wisdom-ui-debug.aar}"

if [[ ! -f "$RELEASE" ]]; then echo "[G8][FAIL] 缺 release AAR: $RELEASE" >&2; exit 1; fi
if [[ ! -f "$DEBUG" ]]; then echo "[G8][FAIL] 缺 debug AAR（G8 正例）: $DEBUG" >&2; exit 1; fi

count_preview() {
  local aar="$1" tmp; tmp="$(mktemp -d)"
  (cd "$tmp" && unzip -qq "$aar" classes.jar && unzip -qq -o classes.jar)
  local n; n="$(grep -rl "Preview" "$tmp" --include="*.class" 2>/dev/null | wc -l | tr -d ' ')"
  rm -rf "$tmp"
  printf '%s' "$n"
}

REL_N="$(count_preview "$RELEASE")"
DBG_N="$(count_preview "$DEBUG")"
if [[ "$REL_N" -ne 0 ]]; then echo "[G8][FAIL] release AAR 含 Preview 类文件计数=$REL_N" >&2; exit 1; fi
if [[ "$DBG_N" -le 0 ]]; then echo "[G8][FAIL] debug AAR 正例缺失：Preview 类文件计数=$DBG_N" >&2; exit 1; fi
echo "[G8] PASS：release Preview=0 且 debug Preview=$DBG_N（正负例均成立）"
