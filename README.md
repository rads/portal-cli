# portal-cli

Open [Portal](https://github.com/djblue/portal) windows from the command-line.

```shell
$ bbin install io.github.rads/portal-cli
{:lib io.github.rads/portal-cli,
 :coords
 {:git/url "https://github.com/rads/portal-cli",
  :git/tag "v0.0.1",
  :git/sha "8f418b335099bd112fa42a5f688a4d6d12432476"}}
Checking out: https://github.com/rads/portal-cli at 8f418b335099bd112fa42a5f688a4d6d12432476

$ portal
Usage: portal.clj <file.(edn|json|xml|yaml)>

# Open a Portal window with all installed scripts
$ portal <(bbin ls)
```
