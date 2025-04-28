package tn.fst.spring.netvoyage.dtos;

public class ChatStatisticsDTO {

    private long totalMessagesSent;
    private long totalMessagesWithForbiddenWords;
    private double forbiddenWordsPercentage;
    private long messagesSentToday;
    private double averageWordsPerMessage;
    private int longestMessageLength;

    public long getTotalMessagesSent() {
        return totalMessagesSent;
    }

    public void setTotalMessagesSent(long totalMessagesSent) {
        this.totalMessagesSent = totalMessagesSent;
    }

    public long getTotalMessagesWithForbiddenWords() {
        return totalMessagesWithForbiddenWords;
    }

    public void setTotalMessagesWithForbiddenWords(long totalMessagesWithForbiddenWords) {
        this.totalMessagesWithForbiddenWords = totalMessagesWithForbiddenWords;
    }

    public double getForbiddenWordsPercentage() {
        return forbiddenWordsPercentage;
    }

    public void setForbiddenWordsPercentage(double forbiddenWordsPercentage) {
        this.forbiddenWordsPercentage = forbiddenWordsPercentage;
    }

    public long getMessagesSentToday() {
        return messagesSentToday;
    }

    public void setMessagesSentToday(long messagesSentToday) {
        this.messagesSentToday = messagesSentToday;
    }

    public double getAverageWordsPerMessage() {
        return averageWordsPerMessage;
    }

    public void setAverageWordsPerMessage(double averageWordsPerMessage) {
        this.averageWordsPerMessage = averageWordsPerMessage;
    }

    public int getLongestMessageLength() {
        return longestMessageLength;
    }

    public void setLongestMessageLength(int longestMessageLength) {
        this.longestMessageLength = longestMessageLength;
    }

    // Constructor
    public ChatStatisticsDTO(long totalMessagesSent, long totalMessagesWithForbiddenWords, double forbiddenWordsPercentage, long messagesSentToday, double averageWordsPerMessage, int longestMessageLength) {
        this.totalMessagesSent = totalMessagesSent;
        this.totalMessagesWithForbiddenWords = totalMessagesWithForbiddenWords;
        this.forbiddenWordsPercentage = forbiddenWordsPercentage;
        this.messagesSentToday = messagesSentToday;
        this.averageWordsPerMessage = averageWordsPerMessage;
        this.longestMessageLength = longestMessageLength;
    }

}
