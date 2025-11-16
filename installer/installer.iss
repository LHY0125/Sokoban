[Setup]
AppName=Sokoban
AppVersion=2.0.0
AppPublisher=LHY Team
AppPublisherURL=https://github.com/LHY0125/Sokoban.git
AppSupportURL=https://github.com/LHY0125/Sokoban.git
AppUpdatesURL=https://github.com/LHY0125/Sokoban.git
DefaultDirName={autopf}\Sokoban
DefaultGroupName=Sokoban
AllowNoIcons=yes
LicenseFile=..\README.md
OutputDir=..\installer\dist
OutputBaseFilename=Sokoban_Inno_Setup
Compression=lzma
SolidCompression=yes
WizardStyle=modern
PrivilegesRequired=admin
SetupIconFile=..\dist\app\Sokoban\Sokoban.ico

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "..\dist\app\Sokoban\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\Sokoban"; Filename: "{app}\Sokoban.exe"; IconFilename: "{app}\Sokoban.ico"
Name: "{group}\{cm:UninstallProgram,Sokoban}"; Filename: "{uninstallexe}"
Name: "{autodesktop}\Sokoban"; Filename: "{app}\Sokoban.exe"; Tasks: desktopicon; IconFilename: "{app}\Sokoban.ico"

[Run]
Filename: "{app}\Sokoban.exe"; Description: "{cm:LaunchProgram,Sokoban}"; Flags: nowait postinstall skipifsilent