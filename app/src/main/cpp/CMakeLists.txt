cmake_minimum_required(VERSION 3.31.5)

add_library(
        constants
        SHARED
        constants.c
)

target_link_libraries(
        constants
        android
        log
)
