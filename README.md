# bittrader
my bitcoin trader

[![Build projects](https://github.com/ken-tunc/bittrader/actions/workflows/build.yml/badge.svg)](https://github.com/ken-tunc/bittrader/actions/workflows/build.yml)
[![codecov](https://codecov.io/gh/ken-tunc/bittrader/branch/main/graph/badge.svg?token=SYOIQR2X1D)](https://codecov.io/gh/ken-tunc/bittrader)

## Deploy
Deploy application to Kubernetes cluster
```
$ kubectl apply -f ./k8s
```

## Build docker images
```
$ ./gradlew bootBuildImage
```

## System architecture
![system architecture](./docs/diagrams/system_architecture.png)
