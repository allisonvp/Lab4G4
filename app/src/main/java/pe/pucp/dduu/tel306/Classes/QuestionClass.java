package pe.pucp.dduu.tel306.Classes;

public class QuestionClass {
    private String id;
    private String questionText;
    private String questionDate;
    private AnswerClass[] answers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionDate() {
        return questionDate;
    }

    public void setQuestionDate(String questionDate) {
        this.questionDate = questionDate;
    }

    public AnswerClass[] getAnswers() {
        return answers;
    }

    public void setAnswers(AnswerClass[] answers) {
        this.answers = answers;
    }

    public String[] forSpinner(){
        String[] ans= new String[answers.length];
        int i=0;
        for (AnswerClass answer : answers){
            ans[i]= answer.getAnswerText();
            i++;
        }
        return ans;
    }
}
