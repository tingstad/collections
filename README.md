collections
===========

Playing with a collections library which uses dynamic proxy objects 
to avoid loops and anonymous classes for common operations.

This enables a more declarative and functional programming style,
perhaps useful for older versions of Java (5-7).

```
Lists.sort(persons).descendingBy().getFirstName();
int total = Lists.sum(persons).getAge();
int lower = Lists.minimum(persons).getAge();
```

