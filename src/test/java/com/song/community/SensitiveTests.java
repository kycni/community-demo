package com.song.community;

import com.song.community.utils.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Kycni
 * @date 2022/2/24 18:44
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityDemoApplication.class)
public class SensitiveTests {
    @Autowired
    private SensitiveFilter sensitiveFilter;
    
    @Test
    public void test () {
        String text = "今天来嫖娼，吸毒所的人来抓我，真倒霉";
        String filterText = sensitiveFilter.filter(text);
        System.out.println(filterText);
    }
}
