package com.franroa.roottranslator.core;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class AlreadyReadWord extends Model {
    public static void addNewWords(Set<String> uniqueWords) {
        try {
            String queryToAlreadyReadWords = "INSERT INTO already_read_words (word) values (?) ON CONFLICT DO NOTHING";
            PreparedStatement psOne = Base.startBatch(queryToAlreadyReadWords);
            Base.connection().setAutoCommit(false);

            for (String word : uniqueWords) {
                psOne.setString(1, word);
                psOne.addBatch();

            }
            psOne.executeBatch();
            Base.connection().commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
