package com.bartlomiejpluta.base.util.random;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

import static java.lang.String.format;

@Data
@AllArgsConstructor
public class DiceRoller {
   private static final Pattern INT_PATTERN = Pattern.compile("[+-]?\\d+");
   private static final Pattern CODE_PATTERN = Pattern.compile("(\\d+)d(\\d+)([+-]\\d+)?");
   private final Random random = new Random();
   private int rolls;
   private int dice;
   private int modifier;

   public int roll() {
      var sum = modifier;

      for (int i = 0; i < rolls; ++i) {
         sum += random.nextInt(dice) + 1;
      }

      return Math.max(0, sum);
   }

   public int min() {
      return Math.max(0, modifier + rolls);
   }

   public int max() {
      return Math.max(0, modifier + rolls * dice);
   }

   public String code() {
      var builder = new StringBuilder();
      if(rolls > 0 && dice > 0) {
         builder.append(format("%dd%d", rolls, dice));
      }

      if(!builder.isEmpty() && modifier != 0) {
         builder
                 .append(modifier > 0 ? "+" : "-")
                 .append(Math.abs(modifier));
      } else if(builder.isEmpty()) {
         builder.append(modifier);
      }

      return builder.toString();
   }

   @Override
   public String toString() {
      return format("%s (min=%d, max=%d)", code(), min(), max());
   }

   public static DiceRoller of(String code) {
      var intMatcher = INT_PATTERN.matcher(code);
      if(intMatcher.matches()) {
         return new DiceRoller(0, 0, Integer.parseInt(code));
      }

      var codeMatcher = CODE_PATTERN.matcher(code);
      if(codeMatcher.matches()) {
         var rolls = Integer.parseInt(codeMatcher.group(1));
         var dice = Integer.parseInt(codeMatcher.group(2));
         var modifier = Integer.parseInt(Optional.ofNullable(codeMatcher.group(3)).orElse("0"));
         return new DiceRoller(rolls, dice, modifier);
      }

      throw new IllegalArgumentException("Argument is supposed to be in RPG dice code format (e.g. 2d4+2) or simple integer");
   }

   public static int roll(String code) {
      return of(code).roll();
   }
}
