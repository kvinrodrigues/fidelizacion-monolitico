import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentType } from '../document-type.model';
import { DocumentTypeService } from '../service/document-type.service';
import { DocumentTypeDeleteDialogComponent } from '../delete/document-type-delete-dialog.component';

@Component({
  selector: 'jhi-document-type',
  templateUrl: './document-type.component.html',
})
export class DocumentTypeComponent implements OnInit {
  documentTypes?: IDocumentType[];
  isLoading = false;

  constructor(protected documentTypeService: DocumentTypeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.documentTypeService.query().subscribe(
      (res: HttpResponse<IDocumentType[]>) => {
        this.isLoading = false;
        this.documentTypes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDocumentType): number {
    return item.id!;
  }

  delete(documentType: IDocumentType): void {
    const modalRef = this.modalService.open(DocumentTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentType = documentType;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
