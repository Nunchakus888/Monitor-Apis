angular.module("ConstantModule",[]).
constant("All" ,{
	Ip: "172.16.73.101:9900",
	WebSockUrl: "ws://172.16.66.170:8787/monitor/123/",
	GatewayKey: ["cdh.realtime.gateway_172.16.66.170:20480_30615","cdh.realtime.gateway"],
	ServiceVirtualNodes: ["fxspot","fxderivative"]
});