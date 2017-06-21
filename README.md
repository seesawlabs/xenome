# xenome

[![Build Status](https://travis-ci.org/seesawlabs/xenome.svg?branch=master)](https://travis-ci.org/seesawlabs/xenome)

Clojure implementation of the [NEAT algorithm](http://nn.cs.utexas.edu/downloads/papers/stanley.ec02.pdf).

## Usage

FIXME

## Development

Refresh all namespaces in dependency order:

```Clojure
; At the REPL
(refresh)
```

All `xenome` namespaces are available from the `user` namespace by their alias:

```Clojure
; xenome.core/some-fn
(x/some-fn)

; clojure.spec.gen.alpha/some-fn
(gen/some-fn)
```

Check the [user.clj](./dev/user.clj) file for a list of aliases available.

Auto run the tests upon file change:

```
lein auto test
```

The `user` namespace automatically calls `(o/instrument)`. Any defined specs will be
instrumented while working at the REPL. Note that the `user` namespace is _not_ loaded
in production builds; it is a development dependency only.

## License

Copyright © 2017 SeeSaw Labs

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
