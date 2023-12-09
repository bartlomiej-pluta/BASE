{
  description = "BASE engine";

  inputs = {
    nixpkgs.url = github:NixOS/nixpkgs/nixos-23.11;
  };

  outputs = { self, nixpkgs }:
  let
    pkgs = import nixpkgs { inherit system; };
    system = "x86_64-linux";
    opengl = "/run/opengl-driver";
  in {
    devShells.${system}.default = with pkgs; mkShell {
      name = "base-editor";

      buildInputs = [
        xorg.libXtst
        alsa-lib
        gradle_7
        jdk17
        protobuf
      ];

      shellHook = ''
        echo
        echo
        echo "======================================================================================================"
        echo "Welcome to BASE NixOS flake shell environment"
        echo "Remember to provide following LD environment variable in order to run application outside this shell:"
        echo
        echo "LD_LIBRARY_PATH=$LD_LIBRARY_PATH"
        echo "======================================================================================================"
        echo
        echo
      '';

      LD_LIBRARY_PATH = "${opengl}/lib:${xorg.libXtst}/lib:${alsa-lib}/lib";
      PROTOBUF_EXECUTABLE = "${protobuf}/bin/protoc";
    };
  };
}
