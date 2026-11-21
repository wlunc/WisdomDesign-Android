#!/usr/bin/env bash
# G6 · Compose metrics 稳定性检查（wisdomCheckComposeMetrics）
# 读 build/compose-metrics/*-module.json；文件缺失即 fail（不允许 skip）。
# inferredUnstableClasses == 0 且 knownUnstableArguments 不超过 baseline.json。
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
METRICS="$ROOT/wisdom-ui/build/compose-metrics"
BASELINE="$METRICS/baseline.json"
if [[ ! -d "$METRICS" ]]; then echo "[G6][FAIL] 缺 metrics 目录: $METRICS" >&2; exit 1; fi
if [[ ! -f "$BASELINE" ]]; then echo "[G6][FAIL] 缺稳定性基线: $BASELINE" >&2; exit 1; fi

python3 - "$METRICS" "$BASELINE" <<'PY'
import glob, json, os, sys
metrics_dir, baseline_path = sys.argv[1], sys.argv[2]
files = sorted(glob.glob(os.path.join(metrics_dir, "*-module.json")))
if not files:
    print("[G6][FAIL] build/compose-metrics/*-module.json 不存在", file=sys.stderr)
    sys.exit(1)
try:
    baseline = json.load(open(baseline_path, encoding="utf-8"))
    limit = int(baseline.get("knownUnstableArguments", {}).get("total", 0))
except Exception as exc:
    print(f"[G6][FAIL] 基线解析失败: {exc}", file=sys.stderr)
    sys.exit(1)
total = 0
for path in files:
    try:
        data = json.load(open(path, encoding="utf-8"))
    except Exception as exc:
        print(f"[G6][FAIL] metrics 解析失败 {path}: {exc}", file=sys.stderr)
        sys.exit(1)
    unstable = int(data.get("inferredUnstableClasses", 0))
    if unstable != 0:
        print(f"[G6][FAIL] {path}: inferredUnstableClasses={unstable} != 0", file=sys.stderr)
        sys.exit(1)
    known = data.get("knownUnstableArguments", {})
    total += sum(int(v) for v in known.values()) if isinstance(known, dict) else int(known)
if total > limit:
    print(f"[G6][FAIL] knownUnstableArguments={total} 超过基线 {limit}", file=sys.stderr)
    sys.exit(1)
print(f"[G6] PASS：{len(files)} 个模块 inferredUnstableClasses=0；knownUnstableArguments={total} <= {limit}")
PY
