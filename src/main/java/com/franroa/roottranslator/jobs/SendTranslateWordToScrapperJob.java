package com.franroa.roottranslator.jobs;

import com.franroa.roottranslator.container.Container;
import com.franroa.roottranslator.core.Temporary;
import com.franroa.roottranslator.queue.Job;
import com.franroa.roottranslator.services.translatorgui.TranslationGuiException;
import com.franroa.roottranslator.services.translatorgui.TranslatorGuiService;
import org.javalite.activejdbc.LazyList;

import javax.ws.rs.InternalServerErrorException;

import static jodd.util.ThreadUtil.sleep;

public class SendTranslateWordToScrapperJob extends Job {
    @Override
    public void handle() {
        LazyList<Temporary> temporalWords = Temporary.findAll();

        if (! temporalWords.isEmpty()) {
            for(Temporary temporalWord: temporalWords) {
                translateWord(temporalWord);
            }
        }
    }

    private void translateWord(Temporary temporalWord) {
        String wordToTranslate = temporalWord.get("word").toString();

        if (! wordToTranslate.equals("")) {
            sleep(5000);
            try {

                String translatedWord = Container.resolve(TranslatorGuiService.class)
                        .translateWord(wordToTranslate, "de", "es");

                if (! wordToTranslate.equals(translatedWord)) {
                    throw new RuntimeException("wordToTranslate and translatedWord should be the same");
                }

                System.out.println(wordToTranslate);

                Temporary.findFirst("word = ?", wordToTranslate).delete();
            } catch (Exception e) {
                if (e instanceof TranslationGuiException) {
                    sleep(150000);
                } else {
                    throw new InternalServerErrorException();
                }
            }
        }
    }
}
