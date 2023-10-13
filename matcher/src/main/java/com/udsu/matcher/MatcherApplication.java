package com.udsu.matcher;

import com.udsu.matcher.repository.TestRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
@RequiredArgsConstructor
public class MatcherApplication implements CommandLineRunner {
    private final TestRepo testRepo;

    public static void main(String[] args) {
        SpringApplication.run(MatcherApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var lim = System.currentTimeMillis();
        Flux.range(1, 100000)
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(i -> testRepo.test())
                .sequential()
                .collectList()
                .map(s -> {
                    var res = System.currentTimeMillis() - lim;
                    System.out.println("COMPLETE " + res);
                    return s;
                })
                .subscribe();
    }
}
