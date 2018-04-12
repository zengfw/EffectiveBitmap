cmake_minimum_required(VERSION 3.4.1)

add_library( effective-bitmap
             SHARED
             src/main/cpp/effective-bitmap.c )


include_directories( src/main/cpp/jpeg/
                     )

add_library(jpegbither SHARED IMPORTED)
set_target_properties(jpegbither
  PROPERTIES IMPORTED_LOCATION
  ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libjpegbither.so)


find_library( log-lib
              log )

find_library( jnigraphics-lib jnigraphics )

target_link_libraries( effective-bitmap
                       jpegbither
                       ${log-lib}
                       ${jnigraphics-lib})