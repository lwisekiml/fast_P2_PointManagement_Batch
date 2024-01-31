package me.wisekim.fcp.job.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class InputExpireSoonPointAlarmCriteriaDateStepListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        // today jobParamater를 가져옵니다.
        // today + 7 한 다음에 그걸  alarmCriteriaDate라는 이름으로 StepExectuionContext에 넣습니다.
        JobParameter todayParameter = stepExecution.getJobParameters().getParameters().get("today");
        if (todayParameter == null) {
            return;
        }
        LocalDate today = LocalDate.parse((String) todayParameter.getValue());
        ExecutionContext context = stepExecution.getExecutionContext();
        context.put("alarmCriteriaDate", today.plusDays(7).format(DateTimeFormatter.ISO_DATE)); // 1 -> 7 이였음
        stepExecution.setExecutionContext(context);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
