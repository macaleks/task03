package ru.otus.task03.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.task03.config.AppProps;
import ru.otus.task03.dao.AnswerDao;
import ru.otus.task03.dao.QuestionDao;
import ru.otus.task03.model.Answer;
import ru.otus.task03.model.Question;
import ru.otus.task03.model.Student;


import java.util.*;

@Service
public class ExamService {

    private final AnswerDao answerDao;
    private final QuestionDao questionDao;
    private final MessageSource messageSource;
    private final AppProps props;

    public ExamService(AnswerDao answerDao, QuestionDao questionDao, MessageSource messageSource, AppProps props) {
        this.answerDao = answerDao;
        this.questionDao = questionDao;
        this.messageSource = messageSource;
        this.props = props;
    }

    public String testStudent() {
        Map<Integer, List<Answer>> allAnswers = answerDao.getMapOfAnswers();
        Map<Integer, Question> allQuestions = questionDao.getMapOfQuestions();
        Student student = new Student();
        Scanner scanner = new Scanner(System.in);

        System.out.println(messageSource.getMessage("greeting", null, props.getLocale()));
        System.out.println(messageSource.getMessage("enterName", null, props.getLocale()));
        student.setName(scanner.next());
        System.out.println(messageSource.getMessage("enterSurname", null, props.getLocale()));
        student.setSurname(scanner.next());

        System.out.println(String.format(messageSource.getMessage("askToAnswerQuestions", null, props.getLocale()),
                student.getName(), student.getSurname()));
        System.out.println();
        for (Question question: allQuestions.values()) {
            System.out.println(String.format("%d. %s", question.getId(), question.getQuestion()));

            List<Answer> answers = allAnswers.get(question.getId());
            long correctAnswers = answers.stream().filter(Answer::isCorrect).count();
            System.out.printf(messageSource.getMessage("selectCorrectAnswers", null, props.getLocale()), correctAnswers);
            System.out.println();

            for (int i = 0; i < answers.size(); i++) {
                System.out.println(String.format("%d. %s", i, answers.get(i).getAnswer()));
            }

            Set<Answer> respond = new HashSet<>();
            for (int i = 0; i < correctAnswers; i++) {
                int var = scanner.nextInt();
                //Check that it is not out of range
                if (var >=0 && var < answers.size()) {
                    respond.add(answers.get(var));
                }
            }

            student.addQuestionResult(question, CheckAnswerService.check(respond, correctAnswers));
        }
        String result = String.format(messageSource.getMessage("studentHas", null, props.getLocale()), student.getName(), student.getSurname());
        if (student.checkIfPassed()) {
            result += messageSource.getMessage("passed", null, props.getLocale());
        } else {
            result += messageSource.getMessage("notPassed", null, props.getLocale());
        }
        return result;
    }
}
