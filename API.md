3. 接口列表
3.1 创建交易
接口说明：创建一条新的交易记录，需要提供交易的基本信息，同时会进行签名验证和黑名单验证。
请求方式：POST
请求路径：http://localhost:8088/api/transactions
请求参数：
| 参数名 | 类型 | 是否必传 | 说明 |
| ---- | ---- | ---- | ---- |
| id | String | 是 | 交易记录号 |
| accountNumber | String | 是 | 账户号码 |
| amount | BigDecimal | 是 | 交易金额，不能超过 10000.00 |
| type | TransactionType | 是 | 交易类型，可选值：DEPOSIT（存款）、WITHDRAW（取款）、TRANSFER（转账） |
| timestamp | LocalDateTime | 是 | 交易时间，格式：yyyy-MM-dd HH:mm:ss |
| sign | String | 是 | 签名信息 |
返回结构说明：
| 字段名 | 类型 | 说明 |
| ---- | ---- | ---- |
| code | int | 响应状态码 |
| message | String | 响应消息 |
| data | Transaction | 交易记录信息 |
返回参考 JSON：
json
{
    "code": 200,
    "message": "交易创建成功",
    "data": {
        "id": "123456",
        "accountNumber": "0001",
        "amount": "100.00",
        "type": "DEPOSIT",
        "timestamp": "2024-01-01 12:00:00"
    }
}
Curl 命令示例：
bash
curl -X POST "http://localhost:8088/api/transactions?id=123456&accountNumber=0001&amount=100.00&type=DEPOSIT&timestamp=2024-01-01 12:00:00&sign=your_signature"
错误码说明：
| 错误码 | 说明 | 可能抛出的异常类 |
| ---- | ---- | ---- |
| 40000 | 未知错误 | Exception |
| 40003 | 无效请求参数 | MethodArgumentNotValidException、MethodArgumentTypeMismatchException、MissingServletRequestParameterException、ConstraintViolationException |
| 40013 | 无效签名 | TransactionSignInvalidateException |
| 40014 | 签名不存在 | TransactionMissSignException |
| 40030 | 重复提交记录 | TransactionDuplicateIdException |
| 40099 | 账号已被列入黑名单 | TransactionBlacklistedException |
3.2 获取单个交易
接口说明：根据交易记录号获取单个交易记录的详细信息。
请求方式：GET
请求路径：http://localhost:8088/api/transactions/{id}
请求参数：
| 参数名 | 类型 | 是否必传 | 说明 |
| ---- | ---- | ---- | ---- |
| id | String | 是 | 交易记录号 |
返回结构说明：
| 字段名 | 类型 | 说明 |
| ---- | ---- | ---- |
| code | int | 响应状态码 |
| message | String | 响应消息 |
| data | Transaction | 交易记录信息 |
返回参考 JSON：
json
{
    "code": 200,
    "message": "获取交易成功",
    "data": {
        "id": "123456",
        "accountNumber": "0001",
        "amount": "100.00",
        "type": "DEPOSIT",
        "timestamp": "2024-01-01 12:00:00"
    }
}
Curl 命令示例：
bash
curl -X GET "http://localhost:8088/api/transactions/123456"
错误码说明：
| 错误码 | 说明 | 可能抛出的异常类 |
| ---- | ---- | ---- |
| 40000 | 未知错误 | Exception |
| 40001 | 交易记录未找到 | TransactionNotFoundException |
3.3 获取所有交易（分页）
接口说明：获取所有交易记录，支持分页查询。
请求方式：GET
请求路径：http://localhost:8088/api/transactions
请求参数：
| 参数名 | 类型 | 是否必传 | 说明 |
| ---- | ---- | ---- | ---- |
| page | int | 否 | 页码，默认值：0 |
| size | int | 否 | 每页数量，默认值：10 |
返回结构说明：
| 字段名 | 类型 | 说明 |
| ---- | ---- | ---- |
| code | int | 响应状态码 |
| message | String | 响应消息 |
| data | List<Transaction> | 交易记录列表 |
| page | int | 当前页码 |
| size | int | 每页数量 |
| totalPages | int | 总页数 |
| totalElements | long | 总记录数 |
返回参考 JSON：
json
{
    "code": 200,
    "message": "获取所有交易成功",
    "data": [
        {
            "id": "123456",
            "accountNumber": "0001",
            "amount": "100.00",
            "type": "DEPOSIT",
            "timestamp": "2024-01-01 12:00:00"
        }
    ],
    "page": 0,
    "size": 10,
    "totalPages": 1,
    "totalElements": 1
}
Curl 命令示例：
bash
curl -X GET "http://localhost:8088/api/transactions?page=0&size=10"
错误码说明：
| 错误码 | 说明 | 可能抛出的异常类 |
| ---- | ---- | ---- |
| 40000 | 未知错误 | Exception |
| 40003 | 无效请求参数 | MethodArgumentNotValidException、MethodArgumentTypeMismatchException、MissingServletRequestParameterException、ConstraintViolationException |
3.4 根据账户获取交易（分页）
接口说明：根据账户号码获取该账户的所有交易记录，支持分页查询。
请求方式：GET
请求路径：http://localhost:8088/api/transactions/account/{accountNumber}
请求参数：
| 参数名 | 类型 | 是否必传 | 说明 |
| ---- | ---- | ---- | ---- |
| accountNumber | String | 是 | 账户号码 |
| page | int | 否 | 页码，默认值：0 |
| size | int | 否 | 每页数量，默认值：10 |
返回结构说明：
| 字段名 | 类型 | 说明 |
| ---- | ---- | ---- |
| code | int | 响应状态码 |
| message | String | 响应消息 |
| data | List<Transaction> | 交易记录列表 |
| page | int | 当前页码 |
| size | int | 每页数量 |
| totalPages | int | 总页数 |
| totalElements | long | 总记录数 |
返回参考 JSON：
json
{
    "code": 200,
    "message": "根据账户获取交易成功",
    "data": [
        {
            "id": "123456",
            "accountNumber": "0001",
            "amount": "100.00",
            "type": "DEPOSIT",
            "timestamp": "2024-01-01 12:00:00"
        }
    ],
    "page": 0,
    "size": 10,
    "totalPages": 1,
    "totalElements": 1
}
Curl 命令示例：
bash
curl -X GET "http://localhost:8088/api/transactions/account/0001?page=0&size=10"
错误码说明：
| 错误码 | 说明 | 可能抛出的异常类 |
| ---- | ---- | ---- |
| 40000 | 未知错误 | Exception |
| 40003 | 无效请求参数 | MethodArgumentNotValidException、MethodArgumentTypeMismatchException、MissingServletRequestParameterException、ConstraintViolationException |
3.5 更新交易
接口说明：根据交易记录号更新交易记录的部分信息，如账户号码和交易金额。
请求方式：PUT
请求路径：http://localhost:8088/api/transactions/{id}
请求参数：
| 参数名 | 类型 | 是否必传 | 说明 |
| ---- | ---- | ---- | ---- |
| id | String | 是 | 交易记录号 |
| accountNumber | String | 是 | 账户号码 |
| amount | BigDecimal | 否 | 交易金额 |
返回结构说明：
| 字段名 | 类型 | 说明 |
| ---- | ---- | ---- |
| code | int | 响应状态码 |
| message | String | 响应消息 |
| data | Transaction | 交易记录信息 |
返回参考 JSON：
json
{
    "code": 200,
    "message": "交易更新成功",
    "data": {
        "id": "123456",
        "accountNumber": "0002",
        "amount": "200.00",
        "type": "DEPOSIT",
        "timestamp": "2024-01-01 12:00:00"
    }
}
Curl 命令示例：
bash
curl -X PUT "http://localhost:8088/api/transactions/123456?accountNumber=0002&amount=200.00"
错误码说明：
| 错误码 | 说明 | 可能抛出的异常类 |
| ---- | ---- | ---- |
| 40000 | 未知错误 | Exception |
| 40001 | 交易记录未找到 | TransactionNotFoundException |
| 40003 | 无效请求参数 | MethodArgumentNotValidException、MethodArgumentTypeMismatchException、MissingServletRequestParameterException、ConstraintViolationException |
| 40031 | 请求账号异常，无权限 | TransactionAccountInvalidateException |
3.6 删除交易
接口说明：根据交易记录号和账户号码删除交易记录。
请求方式：DELETE
请求路径：http://localhost:8088/api/transactions/{id}
请求参数：
| 参数名 | 类型 | 是否必传 | 说明 |
| ---- | ---- | ---- | ---- |
| id | String | 是 | 交易记录号 |
| accountNumber | String | 是 | 账户号码 |
返回结构说明：
| 字段名 | 类型 | 说明 |
| ---- | ---- | ---- |
| code | int | 响应状态码 |
| message | String | 响应消息 |
| data | null | 无返回数据 |
返回参考 JSON：
json
{
    "code": 200,
    "message": "交易删除成功",
    "data": null
}
Curl 命令示例：
bash
curl -X DELETE "http://localhost:8088/api/transactions/123456?accountNumber=0001"
错误码说明：
| 错误码 | 说明 | 可能抛出的异常类 |
| ---- | ---- | ---- |
| 40000 | 未知错误 | Exception |
| 40001 | 交易记录未找到 | TransactionNotFoundException |
| 40031 | 请求账号异常，无权限 | TransactionAccountInvalidateException |
