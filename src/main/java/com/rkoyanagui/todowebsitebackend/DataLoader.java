package com.rkoyanagui.todowebsitebackend;

import com.rkoyanagui.todowebsitebackend.domain.Todo;
import com.rkoyanagui.todowebsitebackend.repository.TodoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner
{
    private final TodoRepository todoRepository;

    @Override
    public void run(ApplicationArguments args)
    {
        var eat = new Todo();
        eat.setDescription("eat");
        eat.setDone(true);

        var sleep = new Todo();
        sleep.setDescription("sleep");
        sleep.setDone(false);

        var play = new Todo();
        play.setDescription("play");
        play.setDone(false);

        var laugh = new Todo();
        laugh.setDescription("laugh");
        laugh.setDone(false);

        todoRepository.saveAllAndFlush(List.of(eat, sleep, play, laugh));
    }
}
