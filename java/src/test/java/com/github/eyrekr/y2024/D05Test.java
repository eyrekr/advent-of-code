package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D05Test extends AocTest {

    D05Test() {
        constructor(D05::new);
        sample("""
                47|53
                97|13
                97|61
                97|47
                75|29
                61|13
                75|53
                29|13
                97|29
                53|29
                61|53
                97|53
                61|29
                47|13
                75|47
                97|75
                47|61
                75|61
                47|29
                75|13
                53|13
                
                75,47,61,53,29
                97,61,53,29,13
                75,29,13
                75,97,47,61,53
                61,13,29
                97,13,75,29,47
                """);
        star1(143L, 6498L);
        star2(123L, 5017L);
    }
}
