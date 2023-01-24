package model.api;

public class QueryString {
    private String parameterName, parameterValue;

    public QueryString(String parameterName, String parameterValue) {
        if(parameterName.contains(" "))throw new IllegalArgumentException();
        if(parameterValue.contains(" "))throw new IllegalArgumentException();
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterValue() {
        return parameterValue;
    }
}
