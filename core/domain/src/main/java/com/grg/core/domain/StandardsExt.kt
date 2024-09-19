package com.grg.core.domain

inline fun <reified T> T?.orElse(other: T): T = this ?: other

inline fun <reified T> T?.orElse(other: () -> T): T = this ?: other()

inline fun < T1,  T2, R> safeLet(t1: T1?, t2: T2?, block: (t1: T1, t2: T2) -> R): R? {
    return t1?.let {
        t2?.let {
            block(t1, t2)
        }
    }
}

inline fun < T1,  T2,  T3, R> safeLet(
    t1: T1?,
    t2: T2?,
    t3: T3?,
    block: (t1: T1, t2: T2, t3: T3) -> R
): R? {
    return t1?.let {
        t2?.let {
            t3?.let {
                block(t1, t2, t3)
            }
        }
    }
}