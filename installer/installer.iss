[Setup]
AppName=Sokoban
AppVersion=2.1.0
AppPublisher=LHY Team
AppPublisherURL=https://github.com/LHY0125/Sokoban.git
AppSupportURL=https://github.com/LHY0125/Sokoban.git
AppUpdatesURL=https://github.com/LHY0125/Sokoban.git
DefaultDirName={autopf}\Sokoban
DefaultGroupName=Sokoban
AllowNoIcons=yes
LicenseFile=LICENSE.txt
OutputDir=dist
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
Source: "..\dist\Sokoban.jar"; DestDir: "{app}\app"; Flags: ignoreversion
Source: "..\dist\app\Sokoban\map\*"; DestDir: "{app}\map"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "..\dist\app\Sokoban\rank\*"; DestDir: "{app}\rank"; Flags: ignoreversion recursesubdirs createallsubdirs

[Dirs]
Name: "{app}\rank"

[Icons]
Name: "{group}\Sokoban"; Filename: "{app}\Sokoban.exe"; IconFilename: "{app}\Sokoban.ico"
Name: "{group}\{cm:UninstallProgram,Sokoban}"; Filename: "{uninstallexe}"
Name: "{autodesktop}\Sokoban"; Filename: "{app}\Sokoban.exe"; Tasks: desktopicon; IconFilename: "{app}\Sokoban.ico"

[Run]
Filename: "{app}\Sokoban.exe"; Description: "{cm:LaunchProgram,Sokoban}"; Flags: nowait postinstall skipifsilent