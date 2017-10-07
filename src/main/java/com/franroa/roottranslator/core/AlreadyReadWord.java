package com.franroa.roottranslator.core;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AlreadyReadWord extends Model {
    public static void addNewWords(HashSet<String> repeatedWords) {
        try {
//            String queryToAlreadyReadWords = "INSERT INTO already_read_words as arw (word, counter_word_times_read) values (?, ?) ON CONFLICT (word) DO UPDATE SET counter_word_times_read = arw.counter_word_times_read + ?";
            String queryToAlreadyReadWords = "INSERT INTO already_read_words as arw (word) values (?) ON CONFLICT (word) DO NOTHING";
            PreparedStatement psOne = Base.startBatch(queryToAlreadyReadWords);

            for (String word : repeatedWords) {

                psOne.setString(1, word);
                psOne.addBatch();

            }

            psOne.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
