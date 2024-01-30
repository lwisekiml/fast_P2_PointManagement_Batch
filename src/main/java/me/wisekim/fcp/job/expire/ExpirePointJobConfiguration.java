package me.wisekim.fcp.job.expire;

import me.wisekim.fcp.job.validator.TodayJobParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpirePointJobConfiguration {
    @Bean
    public Job expirePointJob(
            JobBuilderFactory jobBuilderFactory,
            TodayJobParameterValidator validator,
            Step expirePointStep

    ) {
        return jobBuilderFactory.get("expirePointJob")
                .validator(validator)
                .incrementer(new RunIdIncrementer()) // run.id가 계속 해서 증가해서 job parameter가 중복되지 않게 해줌(즉 Job 중복 실행 가능)
                .start(expirePointStep)
                .build();
    }
}