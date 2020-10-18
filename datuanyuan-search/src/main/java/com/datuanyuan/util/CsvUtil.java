package com.datuanyuan.util;

import com.google.common.collect.Lists;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * CSV文件工具类
 *
 * @author weiyuan
 * @version 1.0
 */
public class CsvUtil {

    /**
     * 读取csv文件内容，结果以List<List<String>>数据结构返回
     *
     * @param filepath
     * @return
     */
    public static List<List<String>> readCSV(String filepath) {
        List<List<String>> sheet = Lists.newArrayList();
        String charset = "utf-8";
        try (CSVReader csvReader = new CSVReaderBuilder(new BufferedReader(new InputStreamReader(new FileInputStream(new File(filepath)), charset))).build()) {
            Iterator<String[]> iterator = csvReader.iterator();
            while (iterator.hasNext()) {
                List<String> row = new ArrayList<String>();
                Arrays.stream(iterator.next()).forEach(s -> row.add(s));
                sheet.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sheet;
    }

    /**
     * 读取csv文件内容，结果以List<List<String>>数据结构返回
     *
     * @param file
     * @return
     */
    public static List<List<String>> readCSV(File file) {
        List<List<String>> sheet = new ArrayList<List<String>>();
        String charset = "utf-8";
        try (CSVReader csvReader = new CSVReaderBuilder(new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))).build()) {
            Iterator<String[]> iterator = csvReader.iterator();
            while (iterator.hasNext()) {
                List<String> row = new ArrayList<String>();
                Arrays.stream(iterator.next()).forEach(s -> row.add(s));
                sheet.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sheet;
    }

    public static void main(String[] args) {
        long i = 0l;
        File directory = new File("D://detectionInfo//");
        File[] files = directory.listFiles();
        for (File f : files) {
            List<List<String>> totalList = CsvUtil.readCSV(f);
            i += totalList.size();
//            for (List<String> rowList : totalList) {
//                String rowString = "";
//                for (String s : rowList) {
//                    rowString += "【" + s.trim() + "】";
//                }
//                System.out.println(rowString + "【end】");
//            }
        }

        System.out.println("总数据量： " + i);
    }
}
