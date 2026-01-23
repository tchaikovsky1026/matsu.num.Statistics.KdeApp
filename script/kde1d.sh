#!/bin/sh

# ===== setup =====
ROOT_PATH="$(cd "$(dirname "$0")" && pwd)"
LIB_PATH="$ROOT_PATH/lib"

MODULE_PATH="$ROOT_PATH;$LIB_PATH;"
MODULE_NAME=matsu.num.Statistics.KdeApp
MAIN_CLASS=matsu.num.statistics.kdeapp.kde1d.Kde1dCliEntryPoint

# ===== execute =====
exec java \
  -p "$MODULE_PATH" \
  -m "$MODULE_NAME/$MAIN_CLASS" \
  "$@"
