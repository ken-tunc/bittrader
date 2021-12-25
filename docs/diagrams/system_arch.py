#!/usr/bin/env python3
from diagrams import Cluster, Diagram
from diagrams.k8s.compute import Pod
from diagrams.k8s.network import Service
from diagrams.k8s.storage import PersistentVolumeClaim
from diagrams.onprem.client import Client
from diagrams.onprem.compute import Server
from diagrams.onprem.database import Mysql

def main():
    with Diagram("System Architecture", show=False) as system_arch:
        with Cluster("k8s Cluster"):
            candle_api_svc = Service("bittrader-candle-api")
            candle_api_pod = Pod("bittrader-candle-api")
            candle_feeder_pod = Pod("bittrader-candle-feeder")

            order_api_svc = Service("bittrader-order-api")
            order_api_pod = Pod("bittrader-order-api")

            web_svc = Service("bittrader-web")
            web_pod = Pod("bittrader-web")

            mysql_svc = Service("bittrader-mysql")
            mysql_pod = Mysql("bittrader-mysql")
            mysql_pvc = PersistentVolumeClaim("bittrader-mysql-pv-claim")

            phpmyadmin_svc = Service("bittrader-phpmyadmin")
            phpmyadmin_pod = Pod("bittrader-phpmyadmin")

        with Cluster("External APIs"):
            bitflyer_http = Server("bitflyer lightning HTTP API")
            bitflyer_realtime = Server("bitflyer lightning Realtime API")

        client = Client("Browser")

        client - web_svc - web_pod - candle_api_svc
        client - phpmyadmin_svc - phpmyadmin_pod - mysql_svc

        candle_api_svc - candle_api_pod - mysql_svc - mysql_pod - mysql_pvc
        candle_feeder_pod - bitflyer_realtime
        candle_feeder_pod - candle_api_svc
        order_api_svc - order_api_pod - bitflyer_http

    system_arch


if __name__ == "__main__":
    main()
