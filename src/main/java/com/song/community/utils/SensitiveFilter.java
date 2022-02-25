package com.song.community.utils;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kycni
 * @date 2022/2/24 16:12
 */
@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    private static final String REPLACEMENT = "***";
    /**
     * 定义根节点
     */
    private final TrieNode rootNode = new TrieNode();

    /**
     * 初始化敏感词文件
     */
    @PostConstruct
    public void init() {
        try (
                // 通过反射获取敏感词文件,将文件转换输入流
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-word.txt");
                // 通过缓冲流读取输入流
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));                                
                ) {
            String keyword;
            // 一个关键词一个关键词的遍历
            while ((keyword = reader.readLine()) != null) {
                // 将敏感词添加到前缀树
                this.addKeyWord(keyword);
            }
        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }

    }

    /**
     * 将敏感词添加到前缀树中节点
     */
    private void addKeyWord(String keyword) {
        // 创建一个指针，默认指向根节点
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            // 遍历关键词得到单个字符
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            // 如果没有扫描到可能的敏感词
            if (subNode == null) {
                // 初始化子节点
                subNode = new TrieNode();
                // 向当前节点中添加子节点
                tempNode.addSubNode(c, subNode);
            }
            // 否则扫描到可能的关键词，将当前节点指向子节点,进入下一轮循环
            tempNode = subNode;
            // 设置循环结束的标识,得到一个完整的敏感词，标记敏感词的最后一个字符
            if (i == keyword.length() - 1) {
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    /**
     * 敏感词过滤方法
     */
    public String filter (String text) {
        // 判断传进来的字符串是否为空
        if (StringUtils.isBlank(text)) {
            return null;
        }
        // 创建指针1，树的节点指针
        TrieNode temptNode = rootNode;
        // 创建指针2, 记录敏感词的开始，一直向前
        int begin = 0;
        // 创建指针3，记录扫描敏感词的指针，摇摆指针
        int position = 0;
        // 返回的字符串结果
        StringBuilder sb = new StringBuilder();
        while (position < text.length()) {
            // 将文本拆分成字符，进行遍历
            char c = text.charAt(position);
            
            // 跳过符号 如：♥吸♥毒♥
            if (isSymbol(c)) {
                // 敏感词外：指针在根节点,未扫描到可能的敏感词
                if (temptNode == null) {
                    // 敏感词外，将这个字符添加到输出的文本中
                    sb.append(c);
                    begin++;
                }
                // 敏感词内，跳过字符，无论是否扫描到可能的敏感词，position都会向前
                position++;
                continue;
            }
            
            // 检查下级节点,如果有，则将指针移动到下级节点，如果没有则返回null，代表不是关键词，装进sb
            temptNode = temptNode.getSubNode(c);

            // 未扫描到从begin开始的敏感词
            if (temptNode == null) {
                sb.append(text.charAt(begin));
                // 扫描下一个位置
                position = ++begin;
                // 重新指向根节点
                temptNode = rootNode;
            } else if (temptNode.isKeyWordEnd()) {
                // 发现一个完整敏感词,将从begin到position的敏感词替换
                sb.append(REPLACEMENT);
                // 扫描下一个位置
                begin = ++position;
            } else {
                // begin在敏感词的开始，发现可能的敏感词，position移动验证
                position++;
            }
        }
        // 将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    /**
     * 判断是否为符号
     */
    private boolean isSymbol (Character c) {
        // c < 0x2E80 || c > 0x9FFF 东亚文字范围之外且不在Ascii表中
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }
    
    /**
     *  定义前缀树结构
     */
    private class TrieNode {
        // 终止条件
        private boolean isKeyWordEnd = false;
        // 定义子节点, 描述树形结构 (字典树,节点存放的是字符和节点信息用HashMap存储)
        private Map<Character,TrieNode> subNodes = new HashMap<>();

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }
        // 添加节点
        public void addSubNode (Character c, TrieNode node) {
            subNodes.put(c,node);
        }
        // 获取节点
        public TrieNode getSubNode (Character c) {
            return subNodes.get(c);
        }
    }
}
