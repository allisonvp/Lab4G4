package pe.pucp.dduu.tel306.Classes;

public class DtoAnswers {
    private QuestionClass question;
    private AnswerStatsClass[] answerstats;

    public QuestionClass getQuestion() {
        return question;
    }

    public void setQuestion(QuestionClass question) {
        this.question = question;
    }

    public AnswerStatsClass[] getAnswerstats() {
        return answerstats;
    }

    public void setAnswerstats(AnswerStatsClass[] answerstats) {
        this.answerstats = answerstats;
    }
}
