/*
 * Copyright 2021 Arman Bilge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package schrodinger.kernel

import cats.Functor
import cats.syntax.all.*

trait Gumbel[F[_], A]:
  def gumbel: F[A]
  def gumbel(location: A, scale: A): F[A]

object Gumbel:
  inline def apply[F[_], A](location: A, scale: A)(using g: Gumbel[F, A]): F[A] =
    g.gumbel(location, scale)

  inline def standard[F[_], A](using g: Gumbel[F, A]): F[A] = g.gumbel

  given [F[_]: Functor](using Exponential[F, Double]): Gumbel[F, Double] with
    def gumbel(location: Double, scale: Double) =
      gumbel.map(_ * scale + location)

    def gumbel = Exponential.standard.map(-Math.log(_))
