# **cobia**

## TODO

- do some improvement work like

## cobia tag

### <cobia:service />

| name |optional | type | description|
| --- | ----- | ----- | --- |
|interface|required|string|Interface implemented by service|
|ref|required|string|bean id of service|
|registry|optional|string|registry address|
|weight|optional|int|weight to invoke the service|
|balanced|optional|boolean|default: false, to detemine whether provider list join loadbalance work of consumer|

#### Important Note
`balanced` should be set to the same value for the same provider, even if providers may be distributed across 
different nodes.

### <cobia:reference /> 

| name |optional | type | description|
| --- | ----- | ----- | --- |
|id|required|string|id of this reference|
|interface|required|string|Interface of reference|
|registry|optional|string|registry method:direct, zookeeper|
|serviceServer|optional|string|`serviceServer` value is provider address(host:port) when `registry` is direct|
|loadbalance|optional|string|loadbalance mode of consumer, which optional value has random, server and so on, default value is `random`.|

### <cobia:registry />

| name |optional | type | description|
| --- | ----- | ----- | --- |
|type|required|string| value(direct, zookeeper) of registry type |
|address|optional|string|address of registry|

