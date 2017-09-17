#Current Directory Z:\Mis proyectos Android\MisNotas\
$Currentlocation=Get-Location
Remove-Item "$Currentlocation\*.iml"
Remove-Item "$Currentlocation\.gradle" -recurse
Remove-Item "$Currentlocation\local.properties" -recurse 
Remove-Item "$Currentlocation\idea\workspace.xml"
Remove-Item "$Currentlocation\.idea\libraries" -recurse 
Remove-Item "$Currentlocation\.DS_Store" -recurse 
Remove-Item "$Currentlocation\build" -recurse 
Remove-Item "$Currentlocation\captures" -recurse
Remove-Item "$Currentlocation\app\build" -recurse 