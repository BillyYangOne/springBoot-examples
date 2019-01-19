
## spring boot scheduler

### spring boot 默认已经实现了，只需要在启动类里面开启即可 @EnableScheduling

### @Scheduled 说明
 	可以接受两种参数，cron 和 fixedRate 
 	- cron="*/6 * * * * ?"
 	- fixedRate = 6000
 		- @Scheduled(fixedRate = 6000) ：上一次开始执行时间点之后6秒再执行
 		- @Scheduled(fixedDelay = 6000) ：上一次执行完毕时间点之后6秒再执行
 		- @Scheduled(initialDelay=1000, fixedRate=6000) ：第一次延迟1秒后执行，之后按fixedRate的规则每6秒执行一次

