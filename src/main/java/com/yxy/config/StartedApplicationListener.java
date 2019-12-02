package com.yxy.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartedApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        String fozu =
                "                   _ooOoo_"+"\n"+
                        "                  o8888888o"+"\n"+
                        "                  88\" . \"88"+"\n"+
                        "                  (| -_- |)"+"\n"+
                        "                  O\\  =  /O"+"\n"+
                        "               ____/`---'\\____"+"\n"+
                        "             .'  \\\\|     |//  `."+"\n"+
                        "            /  \\\\|||  :  |||//  \\"+"\n"+
                        "           /  _||||| -:- |||||-  \\"+"\n"+
                        "           |   | \\\\\\  -  /// |   |"+"\n"+
                        "           | \\_|  ''\\---/''  |   |"+"\n"+
                        "           \\  .-\\__  `-`  ___/-. /"+"\n"+
                        "         ___`. .'  /--.--\\  `. . __"+"\n"+
                        "      .\"\" '<  `.___\\_<|>_/___.'  >'\"\"."+"\n"+
                        "     | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |"+"\n"+
                        "     \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /"+"\n"+
                        "======`-.____`-.___\\_____/___.-`____.-'======"+"\n"+
                        "                   `=---='"+"\n"+
                        "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"+"\n"+
                        "                 佛祖保佑       永无BUG";
        log.info(fozu);
    }
}
