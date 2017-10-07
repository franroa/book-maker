package com.franroa.roottranslator.core;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Table("temporary")
public class Temporary extends Model {
    public static void addWordsIfNotAlreadyRead(HashSet<String> words) {
        try {
            String query = "INSERT INTO temporary (word) SELECT (?) WHERE NOT EXISTS (SELECT word FROM already_read_words WHERE word = (?))";
            PreparedStatement psTwo = Base.startBatch(query);

            for (String word : words) {

                psTwo.setString(1, word);
                psTwo.setString(2, word);
                psTwo.addBatch();

            }

            psTwo.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
