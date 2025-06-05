接口列表

1. 创建交易

接口地址：POST /api/transactions

接口描述：创建一个新的交易记录。

请求参数：

参数名	类型	是否必填	描述
id	String	是	交易记录号
accountNumber	String	是	账户号码
amount	BigDecimal	是	交易金额，不能超过 1000.00
type	TransactionType	是	交易类型，可选值：DEPOSIT（存款）、WITHDRAW（取款）、TRANSFER（转账）
timestamp	LocalDateTime	是	交易时间，格式：yyyy-MM-dd HH:mm:ss
sign	String	是	请求签名
返回数据说明：
返回创建成功的交易记录，包含交易的详细信息。

参数名	类型	描述
id	String	交易唯一标识
accountNumber	String	账户号码
amount	BigDecimal	交易金额
type	TransactionType	交易类型
timestamp	LocalDateTime	交易时间
返回数据示例：


{
    "id": "123",
    "accountNumber": "abc",
    "amount": 100.00,
    "type": "DEPOSIT",
    "timestamp": "2024-01-01 12:00:00"
}
错误码说明：
| 错误码 | 描述 |
| ---- | ---- |
| 40001 | 交易记录未找到 |
| 40030 | 重复提交记录 |
| 40014 | 签名不存在 |
| 40013 | 无效签名 |
| 40099 | 账号已被列入黑名单 |

curl 命令示例：


curl -X POST "http://localhost:8088/api/transactions?id=123&accountNumber=abc&amount=100.00&type=DEPOSIT&timestamp=2024-01-01 12:00:00&sign=xxxxxx"
2. 获取单个交易

接口地址：GET /api/transactions/{id}

接口描述：根据交易记录号获取单个交易记录。

请求参数：
| 参数名 | 类型 | 是否必填 | 描述 |
| ---- | ---- | ---- | ---- |
| id | String | 是 | 交易记录号 |

返回数据说明：
返回指定交易记录的详细信息。
| 参数名 | 类型 | 描述 |
| ---- | ---- | ---- |
| id | String | 交易唯一标识 |
| accountNumber | String | 账户号码 |
| amount | BigDecimal | 交易金额 |
| type | TransactionType | 交易类型 |
| timestamp | LocalDateTime | 交易时间 |

返回数据示例：


{
    "id": "123",
    "accountNumber": "abc",
    "amount": 100.00,
    "type": "DEPOSIT",
    "timestamp": "2024-01-01 12:00:00"
}
错误码说明：
| 错误码 | 描述 |
| ---- | ---- |
| 40001 | 交易记录未找到 |

curl 命令示例：


curl -X GET "http://localhost:8088/api/transactions/123"
3. 获取所有交易（分页）

接口地址：GET /api/transactions

接口描述：获取所有交易记录，支持分页。

请求参数：
| 参数名 | 类型 | 是否必填 | 描述 |
| ---- | ---- | ---- | ---- |
| page | int | 否 | 页码，默认值为 0 |
| size | int | 否 | 每页数量，默认值为 10 |

返回数据说明：
返回指定页码和每页数量的交易记录列表。
| 参数名 | 类型 | 描述 |
| ---- | ---- | ---- |
| id | String | 交易唯一标识 |
| accountNumber | String | 账户号码 |
| amount | BigDecimal | 交易金额 |
| type | TransactionType | 交易类型 |
| timestamp | LocalDateTime | 交易时间 |

返回数据示例：


[
    {
        "id": "123",
        "accountNumber": "abc",
        "amount": 100.00,
        "type": "DEPOSIT",
        "timestamp": "2024-01-01 12:00:00"
    },
    {
        "id": "456",
        "accountNumber": "def",
        "amount": 200.00,
        "type": "WITHDRAW",
        "timestamp": "2024-01-02 13:00:00"
    }
]
错误码说明：无

curl 命令示例：


curl -X GET "http://localhost:8088/api/transactions?page=0&size=10"
4. 根据账户获取交易（分页）

接口地址：GET /api/transactions/account/{accountNumber}

接口描述：根据账户号码获取交易记录，支持分页。

请求参数：
| 参数名 | 类型 | 是否必填 | 描述 |
| ---- | ---- | ---- | ---- |
| accountNumber | String | 是 | 账户号码 |
| page | int | 否 | 页码，默认值为 0 |
| size | int | 否 | 每页数量，默认值为 10 |

返回数据说明：
返回指定账户的交易记录列表，支持分页。
| 参数名 | 类型 | 描述 |
| ---- | ---- | ---- |
| id | String | 交易唯一标识 |
| accountNumber | String | 账户号码 |
| amount | BigDecimal | 交易金额 |
| type | TransactionType | 交易类型 |
| timestamp | LocalDateTime | 交易时间 |

返回数据示例：

json


[
    {
        "id": "123",
        "accountNumber": "abc",
        "amount": 100.00,
        "type": "DEPOSIT",
        "timestamp": "2024-01-01 12:00:00"
    },
    {
        "id": "456",
        "accountNumber": "abc",
        "amount": 200.00,
        "type": "WITHDRAW",
        "timestamp": "2024-01-02 13:00:00"
    }
]
错误码说明：无

curl 命令示例：

bash


curl -X GET "http://localhost:8088/api/transactions/account/abc?page=0&size=10"
5. 更新交易

接口地址：PUT /api/transactions/{id}

接口描述：根据交易记录号更新交易记录。

请求参数：
| 参数名 | 类型 | 是否必填 | 描述 |
| ---- | ---- | ---- | ---- |
| id | String | 是 | 交易记录号 |
| accountNumber | String | 是 | 账户号码 |
| amount | BigDecimal | 否 | 交易金额 |

返回数据说明：
返回更新后的交易记录详细信息。
| 参数名 | 类型 | 描述 |
| ---- | ---- | ---- |
| id | String | 交易唯一标识 |
| accountNumber | String | 账户号码 |
| amount | BigDecimal | 交易金额 |
| type | TransactionType | 交易类型 |
| timestamp | LocalDateTime | 交易时间 |

返回数据示例：

json


{
    "id": "123",
    "accountNumber": "abc",
    "amount": 200.00,
    "type": "DEPOSIT",
    "timestamp": "2024-01-01 12:00:00"
}
错误码说明：

错误码	描述
40001	交易记录未找到
40031	请求账号异常，无权限
curl 命令示例：

bash


curl -X PUT "http://localhost:8088/api/transactions/123?accountNumber=abc&amount=200.00"
6. 删除交易

接口地址：DELETE /api/transactions/{id}

接口描述：根据交易记录号删除交易记录。

请求参数：
| 参数名 | 类型 | 是否必填 | 描述 |
| ---- | ---- | ---- | ---- |
| id | String | 是 | 交易记录号 |
| accountNumber | String | 是 | 账户号码 |

返回数据说明：
删除成功返回 HTTP 状态码 204 No Content，无响应体。

错误码说明：
| 错误码 | 描述 |
| ---- | ---- |
| 40001 | 交易记录未找到 |
| 40031 | 请求账号异常，无权限 |

curl 命令示例：

bash


curl -X DELETE "http://localhost:8088/api/transactions/123?accountNumber=abc"

通用错误码说明

错误码	描述
40000	未知错误
40002	无效交易
40003	无效请求参数
40004	无效请求
