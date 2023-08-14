package io.narsha.spring.jpa.troubleshooting;

import io.narsha.spring.jpa.troubleshooting.entity.ReviewId;
import io.narsha.spring.jpa.troubleshooting.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collection;
import java.util.Set;

@Slf4j
@EnableAspectJAutoProxy
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        // 6746 sql query
        // 5638 unique review entity
        // 4807 unique book
        // 1938 unique user
        // 199 unique country
        // 23 unique region
        // 7 unique continent
       // service.findByUserReview("%Donald%");

        // 1 sql query
        // 5638 unique review entity
        // 4807 unique book
        // 1938 unique user
        // 199 unique country
        // 23 unique region
        // 7 unique continent
        //service.findByUserReviewV2("%Donald%");

        // 1 sql query
        // 20 unique review entity
        // 15 unique book
        // 15 unique user
        // 15 unique country
        // 9 unique region
        // 6 unique continent
        var page = PageRequest.of(0, 20, Sort.Direction.ASC, "book.title", "user.username");
        //service.findByUserReviewV3("%Donald%", page);


        // 1 sql query
        // 5638 unique review entity
        // 4807 unique book
        // 1938 unique user
        // 199 unique country
        // 23 unique region
        // 7 unique continent
        // service.findByUserReviewV4("%Donald%"); // 5638 entities efficient loading (we don't keep everything in a list so the GC can clean what we are not using)

        // create 1 review
        var userId = "AVCGYZL8FQQTD";
        var bookId = "B000H6ENBY";
     //   var record = new NewReviewRecord(userId, bookId, "summary", "description", 5.0);

        // 3 select query
        // 1 insert query
        // 1 unique book
        // 1 unique user
        // 1 unique country
        // 1 unique region
        // 1 unique continent
        // 1 fresh created data
    //     service.save(record);


        // load reference
        // 3 select query
        // 1 insert query
        // 1 unique book
        // 1 unique user
        // 1 unique country
        // 1 unique region
        // 1 unique continent
        // 1 fresh created data
        // because of jpaRepository.save
        // service.save2(record);


        // load reference + em
        // 1 insert query
        // service.save3(record);

        // create 500 review
        var usersId = Set.of("AVCGYZL8FQQTD",
                "A30TK6U7DNS82R",
                "A3UH4UZ4RSVO82",
                "A2MVUWT453QH61",
                "A22X4XUPKF66MR",
                "A2F6NONFUDB6UK",
                "A14OJS0VWMOSWO",
                "A2RSSXTDZDUSH4",
                "A25MD5I2GUIW6W",
                "A3VA4XFS5WNJO3");
        var booksId = Set.of("B0007FTXZQ",
                "B000NPNIVU",
                "B0007E8TSO",
                "0740300075",
                "0141317310",
                "019854765X",
                "097129450X",
                "1571458425",
                "1581154399",
                "193155904X",
                "0451405463",
                "0451201442",
                "B0007SA2NA",
                "031216484X",
                "0373174225",
                "0415123232",
                "094450258X",
                "B000OV37L4",
                "0805068376",
                "0806516380",
                "B000N6KSQW",
                "0060762004",
                "0723248303",
                "B000N6QNHU",
                "0618131183",
                "0525459707",
                "1879570009",
                "0778322971",
                "B0006AW2K0",
                "B0007FJ3YM",
                "B0006BRHIQ",
                "093175903X",
                "B000KIPN9U",
                "B0007IT614",
                "0141008253",
                "0811815145",
                "0393973832",
                "0974385107",
                "B0007EODDE",
                "0373262779",
                "1577689828",
                "B000OTQURY",
                "0836283430",
                "0595319416",
                "0974711004",
                "1567315100",
                "B000FQ55B8",
                "B00087GM02",
                "0736614990",
                "0300047169",
                "B0007BW2QM");

        var toCreate = usersId.stream().map(uId ->
                booksId.stream().map(bId -> new NewReviewRecord(uId, bId, "summary", "description", 5.0)).toList()
        ).flatMap(Collection::stream).toList();

        // 510 insert sql
        // 510 select sql (loading fresh created data)
        //service.saveAll1(toCreate);

        // 510 select sql
        //service.saveAll2(toCreate);
    }
}
