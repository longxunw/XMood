# XMood 数据整理

## 日程

| 数据名    | 类型   | 备注             |
| --------- | ------ | ---------------- |
| id        | int    |                  |
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
| id                 | string  |                    |
| password           | string  |                    |
| token              | string  | 随机生成           |
| profileImg         | blob    | 头像               |
| scheduleBackground | blob    | 课程表背景图片     |
| ==theme==          | string? | theme的名字        |
| isLocalStored      | bool    | 是否仅本地存储数据 |



## 备忘录

| 数据名     | 类型   | 备注                        |
| ---------- | ------ | --------------------------- |
| head       | string |                             |
| bodySimple | string | 预览内容                    |
| updateTime | Date   | yyyy年MM月dd日 最后更新时间 |
| catalog    | string | 归属目录                    |
| body       | string | 具体内容                    |



## 心情

| 数据名   | 类型           | 备注                            |
| -------- | -------------- | ------------------------------- |
| score    | int            |                                 |
| ==type== | string or int? | 心情类型                        |
| day      | Date           | yyyy-MM-dd，不能作为primary key |
| event    | string         | 长文本                          |



## 课程表

| 数据名            | 类型   | 备注                      |
| ----------------- | ------ | ------------------------- |
| id                | int    | primary key               |
| name              | string |                           |
| location          | string |                           |
| weekDay           | int    | 1-5 表示星期几            |
| period            | int    |                           |
| startTime         | long   | HH:mm                     |
| endTime           | long   | HH:mm                     |
| startWeek         | int    |                           |
| endWeek           | int    |                           |
| weekType          | int    | 0没有单双周，1单周，2双周 |
| semesterStartDate | string | yyyy-MM-dd                |

