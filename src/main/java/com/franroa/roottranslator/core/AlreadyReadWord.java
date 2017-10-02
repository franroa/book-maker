package com.franroa.roottranslator.core;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class AlreadyReadWord extends Model {
    public static void addNewWords(Set<String> uniqueWords) {
        try {
            String queryToAlreadyReadWords = "INSERT INTO already_read_words (word) values (?) ON CONFLICT (word) DO UPDATE SET counter_word_times_read = counter_word_times_read + 1";
            PreparedStatement psOne = Base.startBatch(queryToAlreadyReadWords);

            for (String word : uniqueWords) {
                psOne.setString(1, word);
                psOne.addBatch();

            }
            psOne.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
