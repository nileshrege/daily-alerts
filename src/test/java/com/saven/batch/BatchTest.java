package com.saven.batch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by nrege on 1/17/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={ "/spring-beans.xml"
})
public class BatchTest {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;

    @Test
    public void importFile() throws Exception {
        JobParametersBuilder builder = new JobParametersBuilder();
        JobExecution jobExecution = jobLauncher.run(job,
                builder.toJobParameters());
        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());

    }
}

