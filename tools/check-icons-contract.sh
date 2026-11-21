#!/usr/bin/env bash
# 校验 WDIconName.kt 生成物与 wisdomdesign/contracts/icons.json 的 44 条契约一致。
# 只读检查：不修改任何文件。依赖 python3（macOS / Linux 通用）。
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
CONTRACT="$ROOT/wisdomdesign/contracts/icons.json"
GENERATED="$ROOT/android/wisdom-ui/src/main/kotlin/io/github/wlunc/wisdom/foundation/generated/WDIconName.kt"

[[ -f "$CONTRACT" ]] || { echo "[FAIL] 契约不存在: $CONTRACT" >&2; exit 1; }
[[ -f "$GENERATED" ]] || { echo "[FAIL] 生成物不存在: $GENERATED" >&2; exit 1; }
command -v python3 >/dev/null 2>&1 || { echo "[FAIL] 缺少 python3" >&2; exit 1; }

python3 - "$CONTRACT" "$GENERATED" <<'PY'
import json, re, sys

contract_path, generated_path = sys.argv[1], sys.argv[2]
with open(contract_path, encoding='utf-8') as f:
    data = json.load(f)
contract = []
for group in data['groups']:
    for icon in group['icons']:
        contract.append((
            icon['id'],
            icon['android'],
            bool(icon['mirrorsInRTL']),
        ))

with open(generated_path, encoding='utf-8') as f:
    kt = f.read()
pattern = re.compile(r'^\s{4}([A-Z][A-Za-z0-9]*)\("([^"]*)",\s*"([^"]*)",\s*(true|false)\)[,;]\s*$', re.M)
generated = [
    (m.group(2), m.group(3), m.group(4) == 'true', m.group(1))
    for m in pattern.finditer(kt)
]

print('contract entries:', len(contract))
print('generated entries:', len(generated))
errors = []
if len(contract) != 44:
    errors.append(f'契约条数不是 44: {len(contract)}')
if len(generated) != 44:
    errors.append(f'生成物条数不是 44: {len(generated)}')
for idx, (c, g) in enumerate(zip(contract, generated)):
    cid, android, mirror = c
    gid, material, gmirror, gname = g
    if cid != gid:
        errors.append(f'[{idx}] id 不一致: contract={cid} generated={gid}')
    if android != material:
        errors.append(f'[{idx}] android 名不一致: contract={android} generated={material}')
    if mirror != gmirror:
        errors.append(f'[{idx}] mirrorsInRTL 不一致: contract={mirror} generated={gmirror}')
    expected_name = cid[0].upper() + cid[1:]
    if gname != expected_name:
        errors.append(f'[{idx}] 语义名不一致: contract={expected_name} generated={gname}')

if errors:
    for e in errors:
        print('[MISMATCH] ' + e, file=sys.stderr)
    print('[FAIL] WDIconName.kt 与 icons.json 存在不一致', file=sys.stderr)
    sys.exit(1)
print('[PASS] 44 条 id / android 名 / mirrorsInRTL / 语义名 逐条一致')
PY

