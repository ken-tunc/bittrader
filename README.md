# bittrader
my bitcoin trader

[![Build projects](https://github.com/ken-tunc/bittrader/actions/workflows/build.yml/badge.svg)](https://github.com/ken-tunc/bittrader/actions/workflows/build.yml)
[![codecov](https://codecov.io/gh/ken-tunc/bittrader/branch/main/graph/badge.svg?token=SYOIQR2X1D)](https://codecov.io/gh/ken-tunc/bittrader)

## Deploy
1. Build docker images by bootBuildImage gradle task.
```
$ ./gradlew bootBuildImage
```

2. Deploy application to Kubernetes cluster
```
$ kubectl apply -f ./k8s
```

## System architecture
![system architecture](./docs/diagrams/system_architecture.png)
