#!/bin/bash

if [ -z "$1" ]; then
  echo "Usage: $0 <input_directory>"
  exit 1
fi

input_dir="$1" || echo "Le répertoire '$input_dir' n'existe pas."

if [ ! -d "$input_dir" ]; then
  echo "Le répertoire '$input_dir' n'existe pas."
  exit 1
fi

for svg_file in "$input_dir"/*.svg; do
  if [ -f "$svg_file" ]; then
    png_file="${svg_file%.svg}.png"

    magick -background none -density 300 "$svg_file" "$png_file"

    if [ $? -eq 0 ]; then
      echo "Converted: $svg_file -> $png_file"
      rm "$svg_file"
      echo "Deleted: $svg_file"
    else
      echo "conversion error: $svg_file"
    fi
  fi
done

echo "Conversion terminée."