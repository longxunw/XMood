# XMood 数据整理

## 日程

| 数据名    | 类型   | 备注             |
| --------- | ------ | ---------------- |
| id        | int    | primary          |
| day       | long   | yyyy-MM-dd       |
| startTime | long   | HH:mm            |
| endTime   | long   | HH:mm            |
| event     | string |                  |
| isAlarm   | bool   |                  |
| alarmTime | long   | yyyy-MM-dd HH:mm |
| isFinish  | bool   |                  |



## 用户

| 数据名             | 类型    | 备注               |
| ------------------ | ------- | ------------------ |
| id                 | string  | primary key        |
| username           | string  |                    |
| password           | string  |                    |
| token              | string  | 随机生成           |
| profileImg         | blob    | 头像               |
| scheduleBackground | blob    | 课程表背景图片     |
| ==theme==          | string? | theme的名字        |



## 备忘录

| 数据名     | 类型   | 备注                        |
| ---------- | ------ | --------------------------- |
| head       | string |                             |
| updateTime | Date   | yyyy年MM月dd日 最后更新时间 |
| catalog    | string | 归属目录                    |
| body       | string | 具体内容                    |
| id         | int    | primary key                 |



## 心情

| 数据名   | 类型   | 备注                            |
| -------- | ------ | ------------------------------- |
| id    | int    |                 primary key         |
| score    | int    | 心情分数 分数越高心情越好 |
| ==type== | string | 心情类型                        |
| day      | Date   | yyyy-MM-dd HH:mm |
| event    | string | 长文本                          |



## 课程表

##### 单个课程

| 数据名            | 类型   | 备注                      |
| ----------------- | ------ | ------------------------- |
| id                | int    | primary key               |
| name              | string |                           |
| location          | string |                           |
| weekDay           | int    | 0-6 表示星期几            |
| startTime         | long   | HH:mm                     |
| endTime           | long   | HH:mm                     |
| startWeek         | int    |                           |
| endWeek           | int    |                           |
| weekType          | int    | 0没有单双周，1单周，2双周 |

##### 学期信息
| 数据名            | 类型   | 备注                   |
| ----------------- | ------ | ---------------------- |
| semesterStartDate | string | yyyy-MM-dd学期开始日期 |

