set location=%cd%
del /q "%location%\*.iml"
del /s /q /q "%location%\app\*.apk"
del /s /q "%location%\.gradle"
del /s /q "%location%\local.properties"
del "%location%\idea\workspace.xml"
del /s /q "%location%\.idea\libraries"
del /s /q "%location%\.DS_Store"
del /s /q "%location%\build"
del /s /q "%location%\captures"
del /s /q "%location%\app\build"