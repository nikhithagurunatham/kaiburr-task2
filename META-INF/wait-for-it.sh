#!/bin/sh
# wait-for-it.sh

TIMEOUT=15
QUIET=0

while getopts "t:q" opt; do
  case "$opt" in
    t) TIMEOUT=$OPTARG ;;
    q) QUIET=1 ;;
  esac
done

shift $(($OPTIND - 1))

HOST=$1
PORT=$2

if [ -z "$HOST" ] || [ -z "$PORT" ]; then
  echo "Usage: $0 [-t timeout] [-q] host port" >&2
  exit 1
fi

if ! command -v nc >/dev/null; then
  echo "nc (netcat) not found, installing..."
  apt-get update && apt-get install -y netcat
fi

for i in `seq $TIMEOUT` ; do
  if nc -z "$HOST" "$PORT" >/dev/null 2>&1; then
    if [ "$QUIET" -eq 0 ]; then
      echo "Service $HOST:$PORT is available!"
    fi
    exec "$@"
  fi
  sleep 1
done

echo "Timeout reached, service $HOST:$PORT not available" >&2
exit 1